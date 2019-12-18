package com.dt.core.shiro.session;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dt.core.cache.ThreadTaskHelper;
import com.dt.core.dao.Rcd;
import com.dt.core.tool.util.SerializableUtils;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.service.ISysSessionService;
import com.dt.module.db.DB;

/**
 * @author: algernonking
 * @date: 2017年11月7日 下午2:18:05
 * @Description: TODO
 */
public class SessionEntityDao extends EnterpriseCacheSessionDAO {

	@Autowired
	ISysSessionService SysSessionService;
	private static Logger _log = LoggerFactory.getLogger(SessionEntityDao.class);

	@Override
	public Serializable create(Session session) {
		// 先保存到缓存中
		Serializable cookie = super.create(session);
		// 新建一个entity保存到数据库
		SimpleSessionEntity entity = new SimpleSessionEntity();
		entity.setSession(SerializableUtils.serialize(session));
		entity.setCookie(cookie.toString());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(session.getStartTimestamp());
		entity.setStart_time(dateString);
		entity.setIp(session.getHost());
		entity.setToken(cookie.toString());
		entity.save();
		_log.info("create session:" + cookie);
		return cookie;
	}

	@Override
	public void update(Session session) throws UnknownSessionException {
		super.update(session);
		// 如果会话过期,停止 没必要再更新了
		if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
			_log.info("会话无效,不更新存储");
			return;
		}
		SimpleSessionEntity entity = new SimpleSessionEntity();
		entity.setId(session.getId().toString());
		entity.setCookie(session.getId().toString());
		entity.setIp(session.getHost());
		entity.setSession(SerializableUtils.serialize(session));
		entity.update(entity);

	}

	@Override
	public Session readSession(Serializable sessionId) throws UnknownSessionException {
		Session session = null;
		try {
			session = super.readSession(sessionId);
		} catch (Exception e) {
		}
		// 如果session已经被删除，则从数据库中查询session
		if (session == null) {
			_log.info("session:" + sessionId + "尝试恢复session");
			SimpleSessionEntity entity = getEntity(sessionId);
			if (entity != null) {
				try {
					String msg = "session:" + sessionId + "找到";
					session = SerializableUtils.deserialize(entity.getSession());
					if (isExpire(session)) {
						msg = msg + ",已过期";
						// 后期可以判断只对app进行过期处理
						session.touch();
					} else {
						msg = msg + ",未过期";
					}
					_log.info(msg);
					return session;
				} catch (Exception e) {
					_log.info("无法初始化,sessionId:" + sessionId);
				}
			} else {
				_log.info("session:" + sessionId + "未找到保存的session");
			}
		}
		return session;
	}

	private boolean isExpire(Session session) {
		long timeout = session.getTimeout();
		long lastTime = session.getLastAccessTime().getTime();
		long current = new Date().getTime();
		if ((lastTime + timeout) > current) {
			return false;
		}
		return true;
	}

	@Override
	public void delete(Session session) {
		super.delete(session);
		_log.info("delete session,sessionId:" + session.getId());
		ThreadTaskHelper.run(new Runnable() {
			@Override
			public void run() {
				DB.instance().execute("update sys_session set dr=1 where cookie=? ", session.getId().toString());
			}
		});

	}

	private SimpleSessionEntity getEntity(Serializable sessionId) {

		String sql = "select * from sys_session where dr=0 and cookie=?";
		if (ToolUtil.isEmpty(sessionId)) {
			return null;
		}
		Rcd rs = DB.instance().uniqueRecord(sql, sessionId);
		if (ToolUtil.isNotEmpty(rs)) {
			SimpleSessionEntity res = new SimpleSessionEntity();
			res.setCookie(rs.getString("cookie"));
			res.setSession(rs.getString("dtsession"));
			res.setId(rs.getString("id"));
			res.setUser_id(rs.getString("user_id"));
			res.setClient(rs.getString("client"));
			return res;
		} else {
			return null;
		}
	}
}

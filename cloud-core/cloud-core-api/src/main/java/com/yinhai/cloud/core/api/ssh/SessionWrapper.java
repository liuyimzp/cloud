package com.yinhai.cloud.core.api.ssh;

import com.jcraft.jsch.Session;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author: zhaokai
 * @create: 2018-09-05 18:13:35
 */
public class SessionWrapper {

    private Long threadId;
    private Session session;
    private LocalDateTime bindTime;

    public SessionWrapper(Long threadId, Session session,LocalDateTime bindTime) {
        this.threadId = threadId;
        this.session = session;
        this.bindTime = bindTime;
    }

    public LocalDateTime getBindTime() {
        return bindTime;
    }

    public void setBindTime(LocalDateTime bindTime) {
        this.bindTime = bindTime;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SessionWrapper that = (SessionWrapper) o;
        return Objects.equals(threadId, that.threadId) &&
                Objects.equals(session, that.session) &&
                Objects.equals(bindTime, that.bindTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(threadId, session, bindTime);
    }

    @Override
    public String toString() {
        return "SessionWrapper{" +
                "threadId=" + threadId +
                ", session=" + session +
                '}';
    }
}

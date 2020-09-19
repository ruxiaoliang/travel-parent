package org.itcase.session;

import org.itcase.constant.Constant;
import org.itcase.utils.EmptyUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Description：当前前台对象登录会话处理
 */
@Log4j2
@Component
public class SubjectUserContext {

    //可以从spring容器中拿到当前session对象
    @Autowired
    HttpSession session;

    //可以从spring容器中拿到当前request对象
    @Autowired
    HttpServletRequest request;


    //是否是测试环境
    @Value("${context.test}")
    String isTest;

    /**
     * @Description 全局对象获得
     */
    public SubjectUser getSubject() {
        String accessToken = null;
        if (Boolean.parseBoolean(isTest)){
            accessToken ="subjectUser10101";
        }else {
            accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        }
        Object subject = session.getAttribute(accessToken);
        SubjectUser subjectUser = null;
        //进行对象转换，真正需要执行的语句
        if (!EmptyUtil.isNullOrEmpty(subject)&&subject instanceof SubjectUser){
            subjectUser = (SubjectUser) subject;
        }
        log.info("【获得】sessionId:"+session.getId());
        if (Boolean.parseBoolean(isTest)&&!EmptyUtil.isNullOrEmpty(subjectUser)){
            subjectUser.setToken("subjectUser10101");
        }
        return subjectUser;
    }

    /**
     * @Description 全局对象放入session
     */
    public  void createdSubject(String key,Object Principal) {
        if (Boolean.parseBoolean(isTest)){
            session.setAttribute("subjectUser10101", Principal);
        }else {
            session.setAttribute(key, Principal);
        }
        log.info("【创建】sessionId:"+session.getId());
    }

    /**
     * @Description 全局对象session清理
     */
    public  void deleteSubject() {
        String accessToken = null;
        if (Boolean.parseBoolean(isTest)){
            accessToken ="subjectUser10101";
        }else {
            accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        }
        session.removeAttribute(accessToken);
        log.info("【删除】sessionId:"+session.getId());
    }

    /**
     * @Description 是否全局对象存在
     */
    public  boolean existSubject(){
        SubjectUser subjectUser = getSubject();
        if (EmptyUtil.isNullOrEmpty(subjectUser)){
            return false;
        }
        return true;
    }

}

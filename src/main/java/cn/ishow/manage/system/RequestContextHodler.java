package cn.ishow.manage.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class RequestContextHodler {
    private static ThreadLocal<ServletAttribute> threadAttribute = new ThreadLocal<>();

    public static void setAttribute(HttpServletRequest request,HttpServletResponse response){
        threadAttribute.set(new ServletAttribute(request,response,request.getSession()));
    }

    public static ServletAttribute getAttribute(){
        return threadAttribute.get();
    }

    public static void remove(){
        threadAttribute.remove();
    }



    public static class ServletAttribute{
        private HttpServletRequest request;
        private HttpServletResponse response;
        private HttpSession session;

        public ServletAttribute() {
        }

        public ServletAttribute(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
            this.request = request;
            this.response = response;
            this.session = session;
        }

        public HttpServletRequest getRequest() {
            return request;
        }

        public void setRequest(HttpServletRequest request) {
            this.request = request;
        }

        public HttpServletResponse getResponse() {
            return response;
        }

        public void setResponse(HttpServletResponse response) {
            this.response = response;
        }

        public HttpSession getSession() {
            return session;
        }

        public void setSession(HttpSession session) {
            this.session = session;
        }
    }
}

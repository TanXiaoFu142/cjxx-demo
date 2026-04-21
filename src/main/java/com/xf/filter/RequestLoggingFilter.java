//package com.xf.filter;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.xf.entity.MasterEntity;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//public class RequestLoggingFilter implements Filter {
//
//    private static final ThreadLocal<String> REQUEST_CONTENT = new ThreadLocal<>();
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // Initialization code
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//
//        // 复制请求正文
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        InputStream inputStream = httpRequest.getInputStream();
//        byte[] buffer = new byte[1024];
//        int bytesRead;
//        while ((bytesRead = inputStream.read(buffer)) != -1) {
//            byteArrayOutputStream.write(buffer, 0, bytesRead);
//        }
//
//        // 重置输入流并创建一个新的ByteArrayInputStream
//        byteArrayOutputStream.flush();
//        byteArrayOutputStream.close();
//        byte[] requestBodyBytes = byteArrayOutputStream.toByteArray();
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBodyBytes);
//
//        // 将请求体存储在线程局部变量中以供以后使用
//        String requestBodyString = new String(requestBodyBytes, StandardCharsets.UTF_8);
//        REQUEST_CONTENT.set(requestBodyString);
//
//        // 创建一个使用新 ByteArrayInputStream 的新 HttpServletRequest
//        HttpServletRequest wrappedRequest = new WrappedRequest(httpRequest, byteArrayInputStream);
//
//        // 使用包装的请求继续过滤器链
//        chain.doFilter(wrappedRequest, response);
//
//
//        // 如果发生错误，打印出有问题的参数
////        System.out.println("Request Parameters: " + REQUEST_CONTENT.get());
//        try {
//            String jsonString = REQUEST_CONTENT.get().trim();
//            if (jsonString.startsWith("[") && jsonString.endsWith("]")) {
//                List<MasterEntity> entityList = JSONArray.parseArray(jsonString, MasterEntity.class);
//            }else if (jsonString.startsWith("{") && jsonString.endsWith("}")){
//                MasterEntity obj = JSONObject.parseObject(jsonString, MasterEntity.class);
//            }
//        } catch (Exception e) {
//            // Print out the problematic parameter if an error occurs
//            System.out.println("Request Parameters: " + REQUEST_CONTENT.get());
//        }
//
//    }
//
//    @Override
//    public void destroy() {
//        // Cleanup code
//    }
//
//    // A wrapper class to wrap the original HttpServletRequest
//    private static class WrappedRequest extends HttpServletRequestWrapper {
//        private final ByteArrayInputStream byteArrayInputStream;
//
//        public WrappedRequest(HttpServletRequest request, ByteArrayInputStream byteArrayInputStream) {
//            super(request);
//            this.byteArrayInputStream = byteArrayInputStream;
//        }
//
//        @Override
//        public BufferedReader getReader() throws IOException {
//            return new BufferedReader(new InputStreamReader(byteArrayInputStream, StandardCharsets.UTF_8));
//        }
//
//        @Override
//        public ServletInputStream getInputStream() throws IOException {
//            return new WrappedServletInputStream(byteArrayInputStream);
//        }
//
//        // A wrapper class for ServletInputStream
//        private static class WrappedServletInputStream extends ServletInputStream {
//            private final ByteArrayInputStream byteArrayInputStream;
//
//            public WrappedServletInputStream(ByteArrayInputStream byteArrayInputStream) {
//                this.byteArrayInputStream = byteArrayInputStream;
//            }
//
//            @Override
//            public int read() throws IOException {
//                return byteArrayInputStream.read();
//            }
//
//            @Override
//            public boolean isFinished() {
//                return false;
//            }
//
//            @Override
//            public boolean isReady() {
//                return false;
//            }
//
//            @Override
//            public void setReadListener(ReadListener listener) {
//
//            }
//        }
//    }
//}
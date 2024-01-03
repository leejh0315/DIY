package project.DIY.filter;

import java.io.IOException;

import org.springframework.util.PatternMatchUtils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import project.DIY.domain.Member;
import project.DIY.session.SessionVar;

@Slf4j
public class LoginFilter implements Filter {
	
	private static final String[] whiteList = {"/","/join","/login","/admin", "/admin/**"};

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		HttpServletRequest req = (HttpServletRequest)request;
		String uri = req.getRequestURI();
		HttpServletResponse resp = (HttpServletResponse)response;
		
		
		if(isLoginCheckPath(uri)) {
			HttpSession session = req.getSession();
			Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);

			if(session == null || session.getAttribute(SessionVar.LOGIN_MEMBER) == null ||
				!member.getLoginId().equals("admin")) {
				log.info("로그인 없이 접근 시도 {}", uri);
				resp.sendRedirect("/login");
				return;
			}

		}
		
		chain.doFilter(request, response);
	}

	
	private boolean isLoginCheckPath(String requestURI) {
		return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
	}
}


package project.DIY.exception;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import project.DIY.domain.Member;
import project.DIY.session.SessionVar;

@Slf4j
@Controller
@RequestMapping("/error")
public class ErrorController {

    @RequestMapping("/400")
    public String error400(HttpServletRequest req, HttpServletResponse resp, Model model) {
		HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		model.addAttribute("member", member);
        log.info("error 401");
        return "error/400";
    }

    @RequestMapping("/404")
    public String error404(HttpServletRequest req, HttpServletResponse resp,Model model) {
    	HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		model.addAttribute("member", member);
        log.info("error 404");
        return "error/404";
    }
    @RequestMapping("/405")
    public String error405(HttpServletRequest req, HttpServletResponse resp,Model model) {
    	HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		model.addAttribute("member", member);
        log.info("error 405");
        return "error/405";
    }

    @RequestMapping("/500")
    public String error500(HttpServletRequest req, HttpServletResponse resp,Model model) {
    	HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		model.addAttribute("member", member);
        log.info("error 500");
        return "error/500";
    }

}

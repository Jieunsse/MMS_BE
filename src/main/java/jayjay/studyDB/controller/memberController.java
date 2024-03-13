package jayjay.studyDB.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jayjay.studyDB.dto.MemberDTO;
import jayjay.studyDB.service.MemberService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@RequiredArgsConstructor // 생성자 주입시 사용하는 어노테이션.
public class memberController {
    // 생성자 주입
    private final MemberService memberService;

    @GetMapping("/member/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("memberController.save");
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO);
        return "login";
    }


    @GetMapping("/member/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            // login 성공
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return "main";
        } else {
            // login 실패
            return "login";
        }
    }

    @GetMapping("/member/list")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        // 어떠한 html 로 가져갈 데이터가 있다면 model 을 사용하면 된다.
        model.addAttribute("memberList", memberDTOList);
        return "list";
    }

    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model) { // 주소창 속 값을 가져올 때  PathVariable 을 사용한다.
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "detail";
    }

    @GetMapping("/member/update")
    public String updateForm(HttpSession session, Model model) {
        String myEmail = (String) session.getAttribute("loginEmail"); // 객체를 오브젝트 타입안에 담으려고 하기 때문에 다운캐스팅이 필요하다.
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember", memberDTO);
        return "update";
    }


    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "redirect:/member/" + memberDTO.getId(); // 정보 수정이 완료된 상태 페이지를 보여주기 위함. getMapping 재요청.
    }

    @GetMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/member/"; // 리다이렉트 뒤에는 html 파일이 아닌 주소가 온다!
    }

    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세선을 무효화, 적합하지 않게 만듦. 실무에서는 절대 이렇게 로그아웃 안한다.
        return "index";
    }
}






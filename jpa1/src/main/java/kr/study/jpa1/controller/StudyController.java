package kr.study.jpa1.controller;

import kr.study.jpa1.domain.Member;
import kr.study.jpa1.domain.StudyRecode;
import kr.study.jpa1.form.StudyForm;
import kr.study.jpa1.service.MemberService;
import kr.study.jpa1.service.StudyRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {

    private final MemberService memberService;
    private final StudyRecordService recordService;

    @GetMapping
    public String addForm(Model model){
        List<Member> members = memberService.getList();
        if(members == null){
            return "redirect:/member";
        }
        model.addAttribute("members", members);
        model.addAttribute("curdate", LocalDate.now());
        model.addAttribute("curtime", LocalTime.now());

        return "study/addForm";
    }
    @PostMapping
    public String addStudyRocord(StudyForm form ){
        log.trace("form ={}" , form);
        log.trace("localDate={}, localTime={}",LocalDate.now(),LocalTime.now());

        Member member = memberService.findByLoginId(form.getMemberLoginId());
        if(member == null){
            log.error(" { } 로그인 아이디가 존재하지 않음 ",form.getMemberLoginId());
            return "redirect:/";
        }
        recordService.saveRecord(form, member);


        return "redirect:/";
    }
    
    @GetMapping("/records")
    public String getAllList(Model model){
        List<Member> members = memberService.getList();
        if(members == null){
            return "redirect:/";
        }

        List<StudyRecode> list = recordService.getAllRecodes();
        if(list == null){
            return "redirect:/";
        }
        model.addAttribute("list", list);

        return "study/list";
    }

    @GetMapping("/{keyId}")
    public String updateForm(@PathVariable Long keyId, Model model){
        StudyRecode record = recordService.getOneRecord(keyId);
        log.trace("record={}" , record);
        if(record == null){
            return "redirect:/";
        }

        model.addAttribute("record", record);
        model.addAttribute("curdate", LocalDate.now());

        return "study/updateForm";
    }

    @GetMapping("/update")
    public String updateRecode(@ModelAttribute StudyForm form ,@RequestParam Long id){
        log.trace("form={}, id={}", form, id);

        return "redirect:/study/recodes";
    }

}

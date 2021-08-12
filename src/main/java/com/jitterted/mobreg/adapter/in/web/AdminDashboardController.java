package com.jitterted.mobreg.adapter.in.web;

import com.jitterted.mobreg.adapter.DateTimeFormatting;
import com.jitterted.mobreg.domain.Huddle;
import com.jitterted.mobreg.domain.HuddleId;
import com.jitterted.mobreg.domain.HuddleService;
import com.jitterted.mobreg.domain.port.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final HuddleService huddleService;
    private final MemberRepository memberRepository;

    @Autowired
    public AdminDashboardController(HuddleService huddleService,
                                    MemberRepository memberRepository) {
        this.huddleService = huddleService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/dashboard")
    public String dashboardView(Model model, @AuthenticationPrincipal AuthenticatedPrincipal principal) {
        String username = null;
        if (principal instanceof OAuth2User oAuth2User) {
            username = oAuth2User.getAttribute("login");
            model.addAttribute("username", username);
            model.addAttribute("name", oAuth2User.getAttribute("name"));
            model.addAttribute("github_id", oAuth2User.getAttribute("id"));
        }
        List<Huddle> huddles = huddleService.allHuddles();
        List<HuddleSummaryView> huddleSummaryViews = HuddleSummaryView.from(huddles, username);
        model.addAttribute("huddles", huddleSummaryViews);
        model.addAttribute("scheduleHuddleForm", new ScheduleHuddleForm());
        return "dashboard";
    }

    @GetMapping("/huddle/{huddleId}")
    public String huddleDetailView(Model model, @PathVariable("huddleId") Long huddleId) {
        Huddle huddle = huddleService.findById(HuddleId.of(huddleId))
                                     .orElseThrow(() -> {
                                         throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                                     });

        HuddleDetailView huddleDetailView = HuddleDetailView.from(huddle, memberRepository);
        model.addAttribute("huddle", huddleDetailView);
        model.addAttribute("registration", new AdminRegistrationForm(huddle.getId()));

        return "huddle-detail";
    }

    @PostMapping("/schedule")
    public String scheduleHuddle(ScheduleHuddleForm scheduleHuddleForm) {
        ZonedDateTime dateTime = DateTimeFormatting.fromBrowserDateAndTime(
                scheduleHuddleForm.getDate(),
                scheduleHuddleForm.getTime());
        huddleService.scheduleHuddle(scheduleHuddleForm.getName(), dateTime);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/register")
    public String registerParticipant(AdminRegistrationForm adminRegistrationForm) {
        HuddleId huddleId = HuddleId.of(adminRegistrationForm.getHuddleId());
        huddleService.registerParticipant(huddleId,
                                          adminRegistrationForm.getName(),
                                          adminRegistrationForm.getGithubUsername()
        );

        return "redirect:/admin/huddle/" + huddleId.id();
    }
}

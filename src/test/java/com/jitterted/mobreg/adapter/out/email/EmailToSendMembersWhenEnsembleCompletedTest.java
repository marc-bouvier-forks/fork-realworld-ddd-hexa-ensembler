package com.jitterted.mobreg.adapter.out.email;

import com.jitterted.mobreg.application.EnsembleService;
import com.jitterted.mobreg.application.TestEnsembleServiceBuilder;
import com.jitterted.mobreg.application.TestMemberBuilder;
import com.jitterted.mobreg.application.port.Notifier;
import com.jitterted.mobreg.domain.Ensemble;
import com.jitterted.mobreg.domain.EnsembleBuilderAndSaviour;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EmailToSendMembersWhenEnsembleCompletedTest {

    // emails are only sent to Ensemble.acceptedMembers()
    @Test
    public void Emails_only_sent_to_accepted_members() throws Exception {
        EnsembleBuilderAndSaviour ensembleBuilder = new EnsembleBuilderAndSaviour();
        TestMemberBuilder memberBuilder = new TestMemberBuilder();
        Ensemble ensemble = ensembleBuilder.accept(memberBuilder.withEmail("accepted@example.com").buildAndSave())
                                           .accept(memberBuilder.withEmail("accepted2@example.com").buildAndSave())
                                           .decline(memberBuilder.withEmail("declined@example.com").buildAndSave())
                                           .build();
        SpyEmailer spyEmailer = new SpyEmailer();
        Notifier notifier = new EmailNotifier(memberBuilder.memberService(), spyEmailer);

        notifier.ensembleCompleted(ensemble);

        assertThat(spyEmailer.sentEmails())
                .extracting(EmailToSend::recipient)
                .containsOnly("accepted@example.com", "accepted2@example.com");
    }

    @Test
    public void Email_content_is_formatted_correctly() throws Exception {
        EnsembleBuilderAndSaviour ensembleBuilder = new EnsembleBuilderAndSaviour();
        TestMemberBuilder memberBuilder = new TestMemberBuilder();
        Ensemble darkMode = ensembleBuilder
                .named("Ensemble #982")
                .accept(memberBuilder
                                .withFirstName("Ace")
                                .withEmail("ace@example.com")
                                .buildAndSave())
                .build();
        SpyEmailer spyEmailer = new SpyEmailer();
        Notifier notifier = new EmailNotifier(memberBuilder.memberService(), spyEmailer);

        EnsembleService ensembleService = new TestEnsembleServiceBuilder()
                .notifier(notifier)
                .saveEnsemble(darkMode)
                .build();
        ensembleService.completeWith(darkMode.getId(), "https://recording.link/completed");

        assertThat(spyEmailer.sentEmails())
                .containsExactly(new EmailToSend("Ensembler Notification: Ensemble Completed",
                                                 """
                                                 Hi Ace,
                                                 
                                                 Ensemble 'Ensemble #982' has been completed.
                                                 
                                                 The <a href="https://recording.link/completed">video recording</a> is now available.
                                                 """,
                                                 "ace@example.com"));
    }

}
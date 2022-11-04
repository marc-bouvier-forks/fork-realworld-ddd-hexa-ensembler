package com.jitterted.mobreg.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EnsembleMemberRsvpTest {

    @Test
    public void Unregistered_member_is_rsvp_unknown() throws Exception {
        Ensemble ensemble = EnsembleFactory.withStartTimeNow();
        MemberId memberId = MemberId.of(73);

        assertThat(ensemble.rsvpOf(memberId))
                .isEqualByComparingTo(Rsvp.UNKNOWN);
    }

    @Test
    public void Registered_member_is_rsvp_accepted() throws Exception {
        Ensemble ensemble = EnsembleFactory.withStartTimeNow();
        MemberId memberId = MemberId.of(73);

        ensemble.acceptedBy(memberId);

        assertThat(ensemble.rsvpOf(memberId))
                .isEqualByComparingTo(Rsvp.ACCEPTED);
    }

    @Test
    public void Member_who_declines_is_rsvp_declined() throws Exception {
        Ensemble ensemble = EnsembleFactory.withStartTimeNow();
        MemberId memberId = MemberId.of(97);

        ensemble.declinedBy(memberId);

        assertThat(ensemble.rsvpOf(memberId))
                .isEqualByComparingTo(Rsvp.DECLINED);
    }

    @Test
    public void Accepted_member_when_declines_is_rsvp_declined() throws Exception {
        Ensemble ensemble = EnsembleFactory.withStartTimeNow();
        MemberId memberId = MemberId.of(97);
        ensemble.acceptedBy(memberId);

        ensemble.declinedBy(memberId);

        assertThat(ensemble.rsvpOf(memberId))
                .isEqualByComparingTo(Rsvp.DECLINED);
    }

    @Test
    public void Declined_member_when_accepts_and_space_available_is_rsvp_accepted() throws Exception {
        Ensemble ensemble = EnsembleFactory.withStartTimeNow();
        MemberId memberId = MemberId.of(79);
        ensemble.declinedBy(memberId);

        ensemble.acceptedBy(memberId);

        assertThat(ensemble.rsvpOf(memberId))
                .isEqualByComparingTo(Rsvp.ACCEPTED);
    }

}
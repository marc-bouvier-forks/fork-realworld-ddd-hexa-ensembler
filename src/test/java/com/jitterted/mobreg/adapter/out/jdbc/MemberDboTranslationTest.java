package com.jitterted.mobreg.adapter.out.jdbc;

import com.jitterted.mobreg.domain.Member;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class MemberDboTranslationTest {

    @Test
    public void Member_with_roles_translated_to_entity_with_roles() throws Exception {
        Member member = new Member("first", "githubuser", "ROLE_USER", "ROLE_MEMBER");
        var indicativeSentences = new DisplayNameGenerator.IndicativeSentences();

        MemberDbo memberDbo = MemberDbo.from(member);

        assertThat(memberDbo.roles)
                .containsOnly("ROLE_USER", "ROLE_MEMBER");
    }

    @Test
    public void Entity_with_roles_translated_to_member_with_roles() throws Exception {
        MemberDbo memberDbo = new MemberDbo();
        memberDbo.firstName = "first";
        memberDbo.githubUsername = "githubusername";
        memberDbo.timeZone = "Z";
        memberDbo.roles = Set.of("ROLE_ONE", "ROLE_TWO");
        memberDbo.id = 11L;

        Member member = memberDbo.asMember();

        assertThat(member.roles())
                .containsOnly("ROLE_ONE", "ROLE_TWO");
    }

    @Test
    public void Entity_with_email_translated_to_domain_member_with_email() throws Exception {
        MemberDbo memberDbo = new MemberDbo();
        memberDbo.email = "member@example.com";
        memberDbo.firstName = "member";
        memberDbo.githubUsername = "githubusername";
        memberDbo.timeZone = "Z";
        memberDbo.roles = Set.of("ROLE_ONE", "ROLE_TWO");
        memberDbo.id = 31L;

        Member member = memberDbo.asMember();

        assertThat(member.email())
                .isEqualTo("member@example.com");
    }

    @Test
    public void Domain_member_with_email_translated_to_data_entity_with_email() throws Exception {
        Member member = new Member("member", "ghuser", "ROLE_THREE");
        member.changeEmailTo("ghuser@example.com");

        MemberDbo memberDbo = MemberDbo.from(member);

        assertThat(memberDbo.email)
                .isEqualTo("ghuser@example.com");
    }
}

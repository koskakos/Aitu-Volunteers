package com.aitu.volunteers.model.serializer;


import com.aitu.volunteers.model.Team;
import com.aitu.volunteers.model.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class UserSerializer extends StdSerializer<User> {

    public UserSerializer() {
        this(null);
    }

    public UserSerializer(Class<User> t) {
        super(t);
    }

    private final List<String> roleIIN = List.of("APPROLE_Developer");

    @Override
    public void serialize(
            User user, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();

        jgen.writeNumberField("id", user.getId());
        jgen.writeStringField("firstname", user.getUserInfo().getFirstname());
        jgen.writeStringField("lastname", user.getUserInfo().getLastname());
        jgen.writeStringField("gender", user.getUserInfo().getGender().toString());
        jgen.writeStringField("barcode", user.getBarcode());

        jgen.writeNumberField("kazakh", user.getUserInfo().getKazakh());
        jgen.writeNumberField("russian", user.getUserInfo().getRussian());
        jgen.writeNumberField("english", user.getUserInfo().getEnglish());

        if(SecurityContextHolder.getContext().getAuthentication() != null) {
            String userSub = (String) ((Jwt) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getClaims().get("sub");
            if(userSub.equals(user.getUserSub())
                    || SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch((a) -> roleIIN.contains(a.toString()))) {
                jgen.writeStringField("iin", user.getUserInfo().getIIN());
            }
        }
        System.out.println();

        if(user.getCertificate() != null)
            jgen.writeBooleanField("isCertificateApproved", user.getCertificate().getIsApproved());

        if(user.getTeam() != null) {
            jgen.writeFieldName("team");
            jgen.writeStartObject();

            jgen.writeStringField("name", user.getTeam().getName());
            jgen.writeStringField("description", user.getTeam().getName());

            jgen.writeEndObject();
        }

        jgen.writeEndObject();
    }
    @Data
    private class CustomTeam {
        private Long id;
        private String title;
        private String description;

        public CustomTeam(Team team) {
            this.id = team.getId();
            this.title = team.getName();
            this.description = team.getDescription();
        }
    }
}

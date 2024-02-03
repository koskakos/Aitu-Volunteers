package com.aitu.volunteers.model.serializer;


import com.aitu.volunteers.model.Team;
import com.aitu.volunteers.model.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Data;

import java.io.IOException;

public class UserSerializer extends StdSerializer<User> {

    public UserSerializer() {
        this(null);
    }

    public UserSerializer(Class<User> t) {
        super(t);
    }

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

        if(user.getCertificate() != null)
            jgen.writeBooleanField("isCertificateApproved", user.getCertificate().getIsApproved());

        jgen.writeFieldName("teams");
        jgen.writeStartArray();
        if(user.getTeams() != null) {
            user.getTeams().forEach((t) -> {
                try {
                    jgen.writeObject(new CustomTeam(t));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        jgen.writeEndArray();
        
        jgen.writeEndObject();
    }
    @Data
    private class CustomTeam {
        private Long id;
        private String title;
        private String description;

        public CustomTeam(Team team) {
            this.id = team.getId();
            this.title = team.getTitle();
            this.description = team.getDescription();
        }
    }
}

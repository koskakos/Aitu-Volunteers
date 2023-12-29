package com.aitu.volunteers.model.serializer;

import com.aitu.volunteers.model.Team;
import com.aitu.volunteers.model.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Data;

import java.io.IOException;

public class TeamSerializer extends StdSerializer<Team> {

    public TeamSerializer() {
        this(null);
    }

    public TeamSerializer(Class<Team> t) {
        super(t);
    }

    @Override
    public void serialize(
            Team team, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();

        jgen.writeNumberField("id", team.getId());
        jgen.writeStringField("title", team.getTitle());
        jgen.writeStringField("description", team.getDescription());
        jgen.writeFieldName("leader");

        jgen.writeStartObject();
        jgen.writeNumberField("id", team.getLeader().getId());
        jgen.writeStringField("firstName", team.getLeader().getUserInfo().getFirstname());
        jgen.writeStringField("lastName", team.getLeader().getUserInfo().getLastname());
        jgen.writeStringField("phone", team.getLeader().getUserInfo().getPhone());
        jgen.writeEndObject();

        jgen.writeFieldName("members");
        jgen.writeStartArray();

        team.getMembers().forEach((m) -> {
            try {
                jgen.writeObject(new Member(m));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        jgen.writeEndArray();

        jgen.writeEndObject();
    }
    @Data
    private class Member {
        private Long id;
        private String firstName;
        private String lastName;

        public Member(User user) {
            this.id = user.getId();
            this.firstName = user.getUserInfo().getFirstname();
            this.lastName = user.getUserInfo().getLastname();
        }
    }
}

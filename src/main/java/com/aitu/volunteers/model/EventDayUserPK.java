package com.aitu.volunteers.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDayUserPK implements Serializable{
    private EventDay eventDay;
    private User user;
}

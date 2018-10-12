package com.fiberhome.mapps.meeting.entity;

import java.util.List;

public class ClientAgendaInfo
{
    private String         day;
    private List<MtAgenda> agendaList;

    public String getDay()
    {
        return day;
    }

    public void setDay(String day)
    {
        this.day = day;
    }

    public List<MtAgenda> getAgendaList()
    {
        return agendaList;
    }

    public void setAgendaList(List<MtAgenda> agendaList)
    {
        this.agendaList = agendaList;
    }
}

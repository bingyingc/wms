package com.marlabs.Vo;

import com.marlabs.Model.Appointment;

import java.util.List;

public class AppointmentListVo {

    private int total;
    private List<Appointment> appointmentList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }
}

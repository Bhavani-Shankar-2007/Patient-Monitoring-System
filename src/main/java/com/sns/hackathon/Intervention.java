package com.sns.hackathon;

import java.util.List;

public class Intervention {
    private List<String> reminders;
    private List<String> educationalContent;
    private List<String> careTeamNotifications;

    public List<String> getReminders() { return reminders; }
    public void setReminders(List<String> reminders) { this.reminders = reminders; }
    public List<String> getEducationalContent() { return educationalContent; }
    public void setEducationalContent(List<String> educationalContent) { this.educationalContent = educationalContent; }
    public List<String> getCareTeamNotifications() { return careTeamNotifications; }
    public void setCareTeamNotifications(List<String> careTeamNotifications) { this.careTeamNotifications = careTeamNotifications; }
}
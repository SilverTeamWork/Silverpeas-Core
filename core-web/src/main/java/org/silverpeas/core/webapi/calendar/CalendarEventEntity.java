/*
 * Copyright (C) 2000 - 2016 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception. You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.core.webapi.calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.silverpeas.core.admin.user.model.User;
import org.silverpeas.core.calendar.Calendar;
import org.silverpeas.core.calendar.CalendarComponent;
import org.silverpeas.core.calendar.CalendarEvent;
import org.silverpeas.core.calendar.Priority;
import org.silverpeas.core.calendar.VisibilityLevel;
import org.silverpeas.core.date.Period;
import org.silverpeas.core.webapi.base.WebEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.net.URI;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.silverpeas.core.calendar.CalendarEventUtil.formatDateWithOffset;
import static org.silverpeas.core.calendar.CalendarEventUtil.formatTitle;
import static org.silverpeas.core.util.StringUtil.isDefined;

/**
 * It represents the state of a calendar event in a calendar as transmitted within the
 * body of an HTTP response or an HTTP request.
 * @author Yohann Chastagnier
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarEventEntity implements WebEntity {

  private URI eventUri;
  private URI calendarUri;

  private String eventId;
  private String calendarId;
  private String calendarZoneId;
  private String title;
  private String description;
  private String location;
  private boolean onAllDay;
  private String startDate;
  private String endDate;
  private VisibilityLevel visibility;
  private Priority priority;
  private CalendarEventRecurrenceEntity recurrence;
  private List<CalendarEventAttendeeEntity> attendees = new ArrayList<>();
  private String ownerName;
  private Date createDate;
  private Date lastUpdateDate;
  private boolean canBeAccessed;
  private boolean canBeModified;
  private boolean canBeDeleted;

  protected CalendarEventEntity() {
  }

  public static CalendarEventEntity fromEvent(final CalendarEvent event,
      final String componentInstanceId, final ZoneId zoneId) {
    return new CalendarEventEntity().decorate(event, componentInstanceId, zoneId);
  }

  /**
   * Sets a URI to this entity. With this URI, it can then be accessed through the Web.
   * @param eventUri the web entity URI.
   * @return itself.
   */
  @SuppressWarnings("unchecked")
  public CalendarEventEntity withEventURI(final URI eventUri) {
    this.eventUri = eventUri;
    return this;
  }

  /**
   * Sets a URI to linked calendar entity. With this URI, it can then be accessed through the Web.
   * @param calendarUri the linked calendar web entity URI.
   * @return itself.
   */
  @SuppressWarnings("unchecked")
  public CalendarEventEntity withCalendarURI(final URI calendarUri) {
    this.calendarUri = calendarUri;
    return this;
  }

  /**
   * Sets attendees to linked calendar entity.
   * @param attendees the linked calendar event attendees web entity URI.
   * @return itself.
   */
  public CalendarEventEntity withAttendees(
      final List<CalendarEventAttendeeEntity> attendees) {
    this.attendees = attendees;
    return this;
  }

  @Override
  public URI getURI() {
    return getEventUri();
  }

  protected void setURI(final URI uri) {
    setEventUri(uri);
  }

  public URI getEventUri() {
    return eventUri;
  }

  protected void setEventUri(final URI eventUri) {
    this.eventUri = eventUri;
  }

  public URI getCalendarUri() {
    return calendarUri;
  }

  protected void setCalendarUri(final URI calendarUri) {
    this.calendarUri = calendarUri;
  }

  public String getId() {
    return getEventId();
  }

  protected void setId(String eventId) {
    setEventId(eventId);
  }

  public String getEventId() {
    return eventId;
  }

  protected void setEventId(final String eventId) {
    this.eventId = eventId;
  }

  public String getCalendarId() {
    return calendarId;
  }

  protected void setCalendarId(final String calendarId) {
    this.calendarId = calendarId;
  }

  public String getCalendarZoneId() {
    return calendarZoneId;
  }

  protected void setCalendarZoneId(final String calendarZoneId) {
    this.calendarZoneId = calendarZoneId;
  }

  public String getTitle() {
    return title;
  }

  protected void setTitle(final String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  protected void setDescription(String description) {
    this.description = description;
  }

  public String getLocation() {
    return location;
  }

  protected void setLocation(final String location) {
    this.location = location;
  }

  public boolean isOnAllDay() {
    return onAllDay;
  }

  protected void setOnAllDay(final boolean onAllDay) {
    this.onAllDay = onAllDay;
  }

  public String getStartDate() {
    return startDate;
  }

  protected void setStartDate(final String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  protected void setEndDate(final String endDate) {
    this.endDate = endDate;
  }

  public VisibilityLevel getVisibility() {
    return visibility;
  }

  protected void setVisibility(final VisibilityLevel visibility) {
    this.visibility = visibility;
  }

  public Priority getPriority() {
    return priority;
  }

  protected void setPriority(final Priority priority) {
    this.priority = priority;
  }

  public CalendarEventRecurrenceEntity getRecurrence() {
    return recurrence;
  }

  public void setRecurrence(final CalendarEventRecurrenceEntity recurrence) {
    this.recurrence = recurrence;
  }

  public List<CalendarEventAttendeeEntity> getAttendees() {
    return attendees;
  }

  public void setAttendees(final List<CalendarEventAttendeeEntity> attendees) {
    withAttendees(attendees);
  }

  @XmlElement
  public String getOwnerName() {
    return ownerName;
  }

  @XmlElement
  public Date getCreateDate() {
    return createDate;
  }

  public Date getLastUpdateDate() {
    return lastUpdateDate;
  }

  protected void setLastUpdateDate(final Date lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  @XmlElement
  public boolean canBeAccessed() {
    return canBeAccessed;
  }

  @XmlElement
  public boolean canBeModified() {
    return canBeModified;
  }

  @XmlElement
  public boolean canBeDeleted() {
    return canBeDeleted;
  }

  /**
   * Gets the period of the event.
   * @return a period instance.
   */
  @XmlTransient
  Period getPeriod() {
    final Period eventPeriod;
    if (isOnAllDay()) {
      eventPeriod = Period.between(LocalDate.parse(startDate), LocalDate.parse(endDate));
    } else {
      eventPeriod = Period.between(OffsetDateTime.parse(startDate), OffsetDateTime.parse(endDate));
    }
    return eventPeriod;
  }

  /**
   * Get the persistent data representation of an event merged with the entity data.
   * The data of the entity are applied to the returned instance.
   * @return a {@link CalendarEvent} instance.
   */
  @XmlTransient
  CalendarEvent getMergedEvent() {
    final CalendarEvent event;
    if (isDefined(getEventId())) {
      event = CalendarEvent.getById(getEventId());
    } else {
      event = CalendarEvent.on(getPeriod());
    }
    applyOn(event);
    applyOn(event.asCalendarComponent());
    return event;
  }

  /**
   * Get the persistent data representation of an event merged with the entity data.
   * The data of the entity are applied to the returned instance.
   * @param event the event aimed to apply modification.
   */
  @XmlTransient
  void applyOn(final CalendarEvent event) {
    event.withVisibilityLevel(getVisibility());
    if (getRecurrence() != null) {
      getRecurrence().applyOn(event, getPeriod());
    } else {
      event.unsetRecurrence();
    }
  }

  /**
   * Applies to the given calendar component the data of the WEB entity.
   * @param component the component aimed to apply modification.
   */
  @XmlTransient
  void applyOn(final CalendarComponent component) {
    component.setPeriod(getPeriod());
    component.setTitle(getTitle());
    component.setDescription(getDescription());
    component.setLocation(getLocation());
    component.setPriority(getPriority());
    List<String> newAttendeeIds = new ArrayList<>(getAttendees().size());
    for (CalendarEventAttendeeEntity attendeeEntity : getAttendees()) {
      attendeeEntity.addTo(component);
      newAttendeeIds.add(attendeeEntity.getId());
    }
    component.getAttendees().removeIf(attendee -> !newAttendeeIds.contains(attendee.getId()));
  }

  protected CalendarEventEntity decorate(final CalendarEvent calendarEvent,
      final String componentInstanceId, final ZoneId zoneId) {
    User currentUser = User.getCurrentRequester();
    final Calendar calendar = calendarEvent.getCalendar();
    final CalendarComponent component = calendarEvent.asCalendarComponent();
    eventId = calendarEvent.getId();
    calendarId = calendar.getId();
    calendarZoneId = calendar.getZoneId().toString();
    onAllDay = calendarEvent.isOnAllDay();
    startDate = formatDateWithOffset(component, calendarEvent.getStartDate(), zoneId);
    endDate = formatDateWithOffset(component, calendarEvent.getEndDate(), zoneId);
    createDate = calendarEvent.getCreationDate();
    lastUpdateDate = calendarEvent.getLastUpdateDate();
    ownerName = calendarEvent.getCreator().getDisplayedName();
    canBeAccessed = calendarEvent.canBeAccessedBy(currentUser);
    title = formatTitle(component, componentInstanceId, canBeAccessed);
    if (canBeAccessed) {
      description = calendarEvent.getDescription();
      location = calendarEvent.getLocation();
      visibility = calendarEvent.getVisibilityLevel();
      priority = calendarEvent.getPriority();
      recurrence = CalendarEventRecurrenceEntity.from(calendarEvent, zoneId);
      canBeModified = calendarEvent.canBeModifiedBy(currentUser);
      canBeDeleted = calendarEvent.canBeDeletedBy(currentUser);
    } else {
      canBeModified = false;
      canBeDeleted = false;
    }
    return this;
  }

  @Override
  public String toString() {
    return toStringBuilder().toString();
  }

  protected ToStringBuilder toStringBuilder() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("eventUri", getURI());
    builder.append("eventId", getEventId());
    builder.append("calendarId", getCalendarId());
    builder.append("calendarZoneId", getCalendarZoneId());
    builder.append("title", getTitle());
    builder.append("description", getDescription());
    builder.append("startDate", getStartDate());
    builder.append("endDate", getEndDate());
    builder.append("onAllDay", isOnAllDay());
    builder.append("visibility", getVisibility());
    builder.append("priority", getPriority());
    builder.append("recurrence", getRecurrence());
    builder.append("ownerName", getOwnerName());
    builder.append("createDate", getCreateDate());
    builder.append("lastUpdateDate", getLastUpdateDate());
    return builder;
  }
}
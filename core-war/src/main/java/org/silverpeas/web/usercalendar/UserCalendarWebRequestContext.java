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
package org.silverpeas.web.usercalendar;

import org.silverpeas.core.calendar.Calendar;
import org.silverpeas.core.calendar.CalendarEventOccurrence;
import org.silverpeas.core.web.mvc.webcomponent.WebComponentRequestContext;

import javax.ws.rs.WebApplicationException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.Temporal;
import java.util.List;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.silverpeas.core.SilverpeasExceptionMessages.unknown;
import static org.silverpeas.core.util.StringUtil.isDefined;
import static org.silverpeas.core.webapi.calendar.CalendarEventOccurrenceEntity.decodeId;

/**
 * @author Yohann Chastagnier
 */
public class UserCalendarWebRequestContext
    extends WebComponentRequestContext<UserCalendarWebController> {

  private List<Calendar> userCalendars;

  @Override
  public void beforeRequestProcessing() {
    userCalendars = Calendar.getByComponentInstanceId(getComponentInstanceId());
    if (userCalendars.isEmpty()) {
      UserCalendarInitialization.initialize(getComponentInstanceId());
      userCalendars = Calendar.getByComponentInstanceId(getComponentInstanceId());
    }
  }

  /**
   * Gets the user calendars.
   * @return list of calendars.
   */
  private List<Calendar> getUserCalendars() {
    return userCalendars;
  }

  /**
   * Gets the user main calendar.
   * @return the main calendar of the user.
   */
  Calendar getMainUserCalendar() {
    for (Calendar calendar : getUserCalendars()) {
      if (calendar.isMainPersonalOf(getUser())) {
        return calendar;
      }
    }
    throw new WebApplicationException(NOT_FOUND);
  }

  /**
   * Gets the user calendar corresponding to the identifier contained into request as {@code
   * calendarId} parameter name.
   * @return a calendar.
   * @throw WebApplicationException when calendarId is set but is not linked to the current user.
   */
  Temporal getOccurrenceStartDate() {
    String startDate = getRequest().getParameter("occurrenceStartDate");
    if (isDefined(startDate)) {
      if (startDate.contains("T")) {
        return OffsetDateTime.parse(startDate);
      } else {
        return LocalDate.parse(startDate);
      }
    }
    return null;
  }

  /**
   * Gets the user event occurrence corresponding to the identifier contained into request as
   * {@code occurrenceId} parameter name.
   * @return an event.
   * @throw WebApplicationException when calendarId is set but is not linked to the current user.
   */
  CalendarEventOccurrence getUserCalendarEventOccurrenceById() {
    CalendarEventOccurrence event = null;
    String occurrenceId = getPathVariables().get("occurrenceId");
    if (isDefined(occurrenceId)) {
      final String decodedId = decodeId(occurrenceId);
      event = CalendarEventOccurrence.getById(decodedId).orElse(null);
      if (event == null) {
        throw new WebApplicationException(unknown("user calendar", decodedId), NOT_FOUND);
      }
    }
    return event;
  }
}
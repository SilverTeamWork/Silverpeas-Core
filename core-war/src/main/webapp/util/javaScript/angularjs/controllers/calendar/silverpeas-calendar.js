/*
 * Copyright (C) 2000 - 2017 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

(function() {

  var ParticipationCache = SilverpeasCache.extend({
    setParticipants : function(participants) {
      this.put('participants', participants);
    },
    getParticipants : function() {
      return this.get('participants');
    }
  });

  angular.module('silverpeas.controllers').controller('silverpeasCalendarController',
      ['context', '$scope', 'CalendarService', function(context, $scope, CalendarService) {
        $scope.getCalendarService = function() {
          alert('Please implement this method into the child controller.')
        };
        $scope.participation = new ParticipationCache(context.componentUriBase + "_participation");

        $scope.loadCalendarsFromContext = function() {
          return CalendarService.list().then(function(calendars) {
            SilverpeasCalendarTools.decorateCalendars(calendars);
            return calendars;
          });
        };

        $scope.goToPage = function(uri, context) {
          var formConfig = sp.formConfig(uri);
          if (context && context.startMoment) {
            formConfig.withParam("occurrenceStartDate", context.startMoment.format())
          }
          silverpeasFormSubmit(formConfig);
        };

        $scope.reloadEventOccurrence = function(occurrenceUri) {
          if (occurrenceUri) {
            return CalendarService.getEventOccurrenceByUri(occurrenceUri)
                .then(function(reloadedOccurrence) {
                  return CalendarService.getByUri(reloadedOccurrence.calendarUri).then(
                      function(calendar) {
                        reloadedOccurrence.calendar = calendar;
                        return reloadedOccurrence;
                      })
                });
          } else {
            return sp.promise.resolveDirectlyWith(
                SilverpeasCalendarTools.extractEventOccurrenceEntityData());
          }
        };

        $scope.defaultVisibility = SilverpeasCalendarConst.visibilities[0].name;
        $scope.defaultPriority = SilverpeasCalendarConst.priorities[0].name;
      }]);
})();
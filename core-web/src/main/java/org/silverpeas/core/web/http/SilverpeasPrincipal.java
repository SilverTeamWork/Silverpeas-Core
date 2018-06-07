/*
 * Copyright (C) 2000 - 2018 Silverpeas
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

package org.silverpeas.core.web.http;

import org.silverpeas.core.admin.user.model.User;

import java.security.Principal;
import java.util.Objects;

/**
 * A user in Silverpeas implied within a JAAS mechanism (Java Authentication and Authorization
 * System). It is the glue between our own way to authenticate and to authorize a user and JAAS.
 * @author mmoquillon
 */
public class SilverpeasPrincipal implements Principal {

  private final User user;

  public SilverpeasPrincipal(final User user) {
    this.user = user;
  }

  /**
   * Gets the full name of the user behind this principal.
   * @return the name of the authenticated user.
   */
  @Override
  public String getName() {
    return user.getDisplayedName();
  }

  /**
   * Gets the unique identifier of this user in Silverpeas.
   * @return the unique identifier of the user.
   */
  public String getId() {
    return user.getId();
  }

  /**
   * Gets the Silverpeas user behind this principal.
   * @return the user.
   */
  public User getUser() {
    return this.user;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SilverpeasPrincipal)) {
      return false;
    }
    final SilverpeasPrincipal that = (SilverpeasPrincipal) o;
    return Objects.equals(user.getId(), that.user.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(user.getId());
  }
}
  
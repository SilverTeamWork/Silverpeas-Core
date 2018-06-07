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
package org.silverpeas.core.index.indexing.model;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An IndexEntryKey uniquely identifies an entry in the indexes. An IndexEntryKey is set at the
 * index entry creation time : when a Silverpeas component adds a new element or document. This
 * IndexEntryKey will be returned later when the document matches a query. A document in Silverpeas
 * is uniquely identified by:
 * <ul>
 *  <LI>the space name where the element has been created. This space name may be a user id
 *  when the space is the private working space of this user.</LI>
 * <LI>The component name which handles the element. This component name may be an instance name
 * when
 * several instances of the same component live in the same space.</LI>
 * <LI>The object type. The meaning of this type is uniquely determined by the component which
 * handles the object.</LI>
 * <LI>The object id.</LI>
 * </UL>
 */
public final class IndexEntryKey implements Serializable {

  private static final long serialVersionUID = 339617003068469338L;

  private final String component;
  private final String objectType;
  private final String objectId;

  /**
   * Constructs a new {@link IndexEntryKey} instance that refers the specified object of the given
   * type handled in the specified component instance.
   * @param componentId the unique identifier of an application instance in Silverpeas.
   * @param objectType the type of the object concerned by the index entry.
   * @param objectId the unique identifier of the object concerned by the index entry.
   */
  public IndexEntryKey(String componentId, String objectType, String objectId) {
    this.component = componentId;
    this.objectType = objectType;
    this.objectId = objectId;
  }

  /**
   * Return the name of the component's instance which handles the object.
   */
  public String getComponent() {
    return component;
  }

  /**
   * Return the type of the indexed document. The meaning of this type is uniquely determined by the
   * component handling the object.
   */
  public String getObjectType() {
    return objectType;
  }

  /**
   * Return the object id.
   */
  public String getObjectId() {
    return objectId;
  }

  /**
   * Returns a string which can be used later to recontruct the key with the create method.
   */
  @Override
  public String toString() {
    return getComponent() + "|" + getObjectType() + "|" + getObjectId();
  }

  /**
   * To be equal two IndexEntryKey must have the same four parts (space, component, type, id). The
   * equals method is redefined so IndexEntryKey objects can be put in a Set or used as Map key.
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof IndexEntryKey) {
      IndexEntryKey k = (IndexEntryKey) o;
      return this.toString().equals(k.toString());
    }
    return false;
  }

  /**
   * Returns the hash code of the String representation. The hashCode method is redefined so
   * IndexEntryKey objects can be put in a Set or used as Map key.
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  /**
   * <p>
   * Create a new {@link IndexEntryKey} from a text representation of it.
   * The text representation of the index entry key should be formatted as following:
   * <code>COMPONENT_INSTANCE_ID|OBJECT_TYPE|OBJECT_ID</code> with
   * </p>
   * <ul>
   *  <li>COMPONENT_INSTANCE_ID the unique identifier of an application instance in Silverpeas,</li>
   *  <li>OBJECT_TYPE the type of the object being indexed,</li>
   *  <li>OBJECT_ID the unique identifier of the object being indexed.</li>
   * </ul>
   * <p>
   * For example <code>COMP||ID</code> must give COMP as component instance identifier and ID as
   * the object identifier but without any object type given.
   * </p>
   * <p>We must have:
   * <code><PRE>
   * create(s).toString().equals(s)
   * </PRE></code>
   * </p>
   * @param s the {@link String} representation of a key of an index entry.
   */
  public static IndexEntryKey create(String s) {
    final Pattern keyPattern = Pattern.compile("^(.*)\\|(.*)\\|(.*)$");
    final Matcher matcher = keyPattern.matcher(s);
    if (matcher.matches()) {
      final String instanceId = matcher.group(1);
      final String objectType = matcher.group(2);
      final String objectId   = matcher.group(3);

      return new IndexEntryKey(instanceId, objectType, objectId);
    }
    throw new IllegalArgumentException("The argument " + s + " isn't correctly formatted!");
  }

}

/*
 * Copyright (C) 2000 - 2014 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have recieved a copy of the text describing
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
package org.silverpeas.initialization;

import org.silverpeas.util.ServiceProvider;

import javax.servlet.ServletContextEvent;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides the method to initialize all the services that implement the
 * {@code org.silverpeas.initialization.Initialization} interface when the application is starting.
 * @author mmoquillon
 */
public class SilverpeasServiceInitialization {

  private static Logger logger =
      Logger.getLogger(SilverpeasServiceInitialization.class.getSimpleName());

  public static void start() {
    logger.log(Level.INFO, "Silverpeas Service Initialization...");
    Set<Initialization> initializations = ServiceProvider.getAllServices(Initialization.class);
    initializations.stream().forEach(initialization -> {
      String simpleClassName = initialization.getClass().getSimpleName();
      try {
        logger.log(Level.INFO, " -> {0} initialization...", simpleClassName);
        initialization.init();
        logger.log(Level.INFO, "    {0} initialization done.", simpleClassName);
      } catch (Exception e) {
        logger.log(Level.SEVERE, "    {0} initialization failure!",
            initialization.getClass().getName());
        throw new RuntimeException(e.getMessage(), e);
      }
    });
    logger.log(Level.INFO, "Silverpeas Service Initialization done.");
  }

  public static void stop() {
    logger.log(Level.INFO, "Silverpeas Service Release...");
    Set<Initialization> initializations = ServiceProvider.getAllServices(Initialization.class);
    for (Initialization initialization : initializations) {
      try {
        initialization.release();
      } catch (Exception ex) {
        logger.log(Level.WARNING, ex.getMessage(), ex);
      }
    }
    logger.log(Level.INFO, "Silverpeas Service Release done.");
  }
}
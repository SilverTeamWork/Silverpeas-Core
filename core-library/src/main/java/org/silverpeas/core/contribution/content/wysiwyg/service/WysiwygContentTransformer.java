/*
 * Copyright (C) 2000 - 2020 Silverpeas
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
package org.silverpeas.core.contribution.content.wysiwyg.service;

import org.silverpeas.core.SilverpeasException;
import org.silverpeas.core.contribution.content.wysiwyg.service.directive.ImageUrlAccordingToHtmlSizeDirective;
import org.silverpeas.core.contribution.content.wysiwyg.service.directive.SilverpeasLinkCssApplierDirective;
import org.silverpeas.core.contribution.content.wysiwyg.service.directive.VariablesReplacementDirective;
import org.silverpeas.core.contribution.content.wysiwyg.service.process.MailContentProcess;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides method to transform wysiwyg content source into other wysiwyg formats.
 * @author Yohann Chastagnier
 */
public class WysiwygContentTransformer {

  private final String wysiwygContent;
  private final List<WysiwygContentTransformerDirective> directives = new ArrayList<>();

  /**
   * An instance of WYSIWYG transformer on the given content.
   * @param wysiwygContent the wysiwyg content.
   * @return an instance of the transformer.
   */
  public static WysiwygContentTransformer on(String wysiwygContent) {
    return new WysiwygContentTransformer(wysiwygContent);
  }

  /**
   * Hidden constructor.
   * @param wysiwygContent the wysiwyg content.
   */
  private WysiwygContentTransformer(final String wysiwygContent) {
    this.wysiwygContent = wysiwygContent;
  }

  /**
   * Transforms all URL of images to take into account theirs display size.
   * @return the instance of the current {@link WysiwygContentTransformer}.
   */
  public WysiwygContentTransformer modifyImageUrlAccordingToHtmlSizeDirective() {
    directives.add(new ImageUrlAccordingToHtmlSizeDirective());
    return this;
  }

  /**
   * Replaces all variables element by theirs corresponding values.
   * @return the instance of the current {@link WysiwygContentTransformer}.
   */
  public WysiwygContentTransformer resolveVariablesDirective() {
    directives.add(new VariablesReplacementDirective());
    return this;
  }

  /**
   * Applies the sp-permalink or sp-link css classes to links HTML elements.
   * @return the instance of the current {@link WysiwygContentTransformer}.
   */
  public WysiwygContentTransformer applySilverpeasLinkCssDirective() {
    directives.add(new SilverpeasLinkCssApplierDirective());
    return this;
  }

  /**
   * Default method in order to apply all the transformation directives and recover immediately the
   * result as string.
   * @return the transformed wysiwyg content.
   */
  public String transform() {
    String transformedWysiwyg = wysiwygContent;
    for (WysiwygContentTransformerDirective directive : directives) {
      transformedWysiwyg = directive.execute(transformedWysiwyg);
    }
    return transformedWysiwyg;
  }

  /**
   * Applies all the transformation directives and finally processing the given treatment.
   * @param process the process to execute after all the directives.
   * @param <R> the result type of the process.
   * @return the result of the process execution.
   * @throws SilverpeasException on technical error.
   */
  public <R> R transform(WysiwygContentTransformerProcess<R> process) throws SilverpeasException {
    return process.execute(transform());
  }

  /**
   * Transforms all referenced content links in order to be handled in mail sending. A content
   * can be for example an attachment.<br>
   * The directive set by method {@link #modifyImageUrlAccordingToHtmlSizeDirective()} is applied.
   * @return the wysiwyg content transformed to be sent by mail.
   * @throws SilverpeasException on technical error.
   */
  public MailContentProcess.MailResult toMailContent() throws SilverpeasException {
    return modifyImageUrlAccordingToHtmlSizeDirective()
        .resolveVariablesDirective()
        .transform(new MailContentProcess());
  }
}

package org.silverpeas.core.workflow.engine.instance;

import org.silverpeas.core.persistence.datasource.repository.jpa.BasicJpaEntityRepository;

import javax.inject.Singleton;

/**
 * Created by Nicolas on 08/06/2017.
 */
@Singleton
public class HistoryStepRepository extends BasicJpaEntityRepository<HistoryStepImpl> {

}
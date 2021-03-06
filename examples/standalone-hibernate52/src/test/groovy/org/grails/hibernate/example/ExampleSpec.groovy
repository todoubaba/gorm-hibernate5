package org.grails.hibernate.example

import grails.gorm.annotation.Entity
import grails.transaction.Rollback
import org.grails.orm.hibernate.HibernateDatastore
import org.springframework.transaction.PlatformTransactionManager
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by graemerocher on 13/07/2016.
 */
class ExampleSpec extends Specification {

    @Shared @AutoCleanup HibernateDatastore hibernateDatastore
    @Shared PlatformTransactionManager transactionManager

    void setupSpec() {
       hibernateDatastore = new HibernateDatastore(Example)
       transactionManager = hibernateDatastore.getTransactionManager()
    }

    @Rollback
    void "test execute Hibernate standalone in a unit test"() {
        when:
        def e = new Example(name: "Fred").save(flush:true)
        hibernateDatastore.sessionFactory.currentSession.clear()
        e = Example.load(e.id)
        then:
        e.name == "Fred"
        Example.count() == 1
    }
}

@Entity
class Example {
    String name
}

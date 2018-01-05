/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
@XmlJavaTypeAdapters({
     @XmlJavaTypeAdapter(
             type = LocalDate.class,
             value = LocalDateAdapter.class
     )
})

package imp.core.entity.conversation;

/**
 * Domain classes used to produce the JSON output for the RESTful services. 
 *
 * These classes contain the JAXB annotations.
 *
 * @author alexis
 */
import imp.core.adapter.LocalDateAdapter;
import java.time.LocalDate;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.adapter;

import java.time.LocalDate;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * XmlAdapter used for Json serialization and deserialization of all LocalDate java objects.
 * 
 * @author alexis
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String string) throws Exception {
        return (string == null ? null : LocalDate.parse(string));
    }

    @Override
    public String marshal(LocalDate date) throws Exception {
        return (date == null ? null : date.toString());
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.bean;

import com.google.gson.Gson;
import imp.core.entity.Matching;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author alexis
 */
@Stateless
public class MatchingRepository extends AbstractRepository<Matching> {
    
    @PersistenceContext(unitName = "imp-pu")
    private EntityManager em;

    public MatchingRepository() {
        super(Matching.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Return all matchings for this post.
     *
     * @param postId The post id.
     * @return
     */
    public JSONArray getByPostId(Long postId) throws ParseException {
        List<Matching> matchings = em
                .createNamedQuery("Matching.findByPost", Matching.class)
                .setParameter("id", postId)
                .getResultList();

        // sort by percent
        Collections.sort(matchings, new Comparator<Matching>() {
            @Override
            public int compare(Matching o1, Matching o2) {
                if (o1.getPercent() > o2.getPercent()) {
                    return -1;
                } else {
                    return 1;
                }
            }

        });

        // product json
        JSONArray json = new JSONArray();
        for (Matching matching : matchings) {
            // generate json
            JSONObject obj = new JSONObject();

            String candidateJSON = (new Gson()).toJson(matching.getCandidate());
            JSONParser parser = new JSONParser();

            obj.put("candidate", (JSONObject) parser.parse(candidateJSON));
            obj.put("percent", matching.getPercent());

            json.add(obj);
        }
        return json;
    }
    
}

package nl.hu.cisq1.lingo.words.data;

import nl.hu.cisq1.lingo.words.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * We depend on an interface,
 * Spring generates an implementation based on our configured adapters
 * (see: application.properties and pom.xml)
 */
public interface SpringWordRepository extends JpaRepository<Word, String> {
    @Query(nativeQuery=true, value="SELECT * FROM words w WHERE w.length = ?1 ORDER BY random() LIMIT 1")
    Optional<Word> findRandomWordByLength(Integer length);

    @Query(nativeQuery=true, value="select case when count(*)> 0 then true else false end from words w where lower(w.word) like lower(:word)")
    boolean checkWordExists(@Param("word") String word);


}

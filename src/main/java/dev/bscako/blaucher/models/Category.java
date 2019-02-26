/**
 *
 */
package dev.bscako.blaucher.models;

import lombok.Data;
import org.hibernate.annotations.NaturalIdCache;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author BSCAKO
 *
 */

@Entity
@Table(name = "categories")
@NaturalIdCache
@Data
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
    private List<Application> applications = new ArrayList<>();

}

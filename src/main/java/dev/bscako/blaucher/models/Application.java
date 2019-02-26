package dev.bscako.blaucher.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Table(name = "applications")
@NaturalIdCache
@NoArgsConstructor
@AllArgsConstructor()
@Data
public class Application implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String exePath;

    @Column(nullable = false)
    private String exeStartIn;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RunMode runMode = RunMode.NORMAL;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "category_application", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "application_id"))
    private List<Category> categories = new ArrayList<>();

    public Application(String name, String exePath, String exeStartIn, RunMode runMode, List<Category> categories) {
        this.name = name;
        this.exePath = exePath;
        this.exeStartIn = exeStartIn;
        this.runMode = runMode;
        this.categories = categories;
    }

    @Override
    public String toString() {
        return name;
    }
}

package mm.com.spring.ak.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="sb_role")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 464598758195632733L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
    private Long id;
	
    @Column(name="role_name")
    private String roleName;

    @Column(name="description")
    private String description;
}

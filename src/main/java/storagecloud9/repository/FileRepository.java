package storagecloud9.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import storagecloud9.entity.File;
import storagecloud9.entity.User;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByFilenameAndUser(String filename, User user);

    boolean existsByFilenameAndUser(String filename, User user);

    @Query("SELECT f FROM File f WHERE f.user = :user ORDER BY f.createdAt DESC")
    Page<File> findLatestFilesByUser(@Param("user") User user, Pageable pageable);

    Page<File> findByUser(User user, Pageable pageable);

    @Query(value = "SELECT * FROM files f WHERE f.user_id = :userId ORDER BY f.created_at DESC LIMIT :limit", nativeQuery = true)
    List<File> findTopNByUserNative(@Param("userId") Long userId, @Param("limit") int limit);

    @Modifying
    @Query("DELETE FROM File f WHERE f.user = :user")
    void deleteAllByUser(@Param("user") User user);

    @Query("SELECT f FROM File f WHERE f.user = :user ORDER BY f.createdAt DESC")
    List<File> findTopNByUser(@Param("user") User user, Pageable pageable);
}
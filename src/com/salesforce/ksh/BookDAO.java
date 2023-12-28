package com.salesforce.ksh;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String JDBC_USER = "webdb";
    private static final String JDBC_PASSWORD = "cnltkqud2131";


    public List<BookVO> searchBooks(String keyword) {
        List<BookVO> result = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT * FROM Book b, Author a " +
                           "WHERE b.AUTHOR_ID = a.AUTHOR_ID " +
                           "AND (b.title LIKE ? OR b.pubs LIKE ? OR a.author_name LIKE ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");
                pstmt.setString(3, "%" + keyword + "%");

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        BookVO book = new BookVO(
                                rs.getInt("book_id"),
                                rs.getString("title"),
                                rs.getString("pubs"),
                                rs.getString("pub_date"),
                                rs.getString("author_name")
                        );
                        result.add(book);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
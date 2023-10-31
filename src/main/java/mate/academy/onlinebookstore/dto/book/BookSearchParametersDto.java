package mate.academy.onlinebookstore.dto.book;

public record BookSearchParametersDto(String[] author, String[] price, String[] title) {
}

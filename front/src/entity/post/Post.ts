import { DateTimeFormatter, LocalDate } from "@js-joda/core";
import { Transform } from "class-transformer";

class Post {
    public postId: number = 0;
    public title: string = "";
    public content: string = "";

    @Transform(({ value }) => LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd")), {
        toClassOnly: true,
    })
    public regDate: LocalDate = LocalDate.now();

    public getRegDateFormatted = () => {
        return this.regDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    };
}

export default Post;

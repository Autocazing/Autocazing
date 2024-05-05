import { useQuery } from "@tanstack/react-query";
import axios from "axios";

const DashboardServer = () => {
    console.log("하위");

    const fetchPost = () => {
        return axios.get("http://localhost:3004/posts");
    };

    const { isLoading, data, isError, error } = useQuery({
        queryKey: ["posts"],
        queryFn: fetchPost,
    });
    console.log("ddd", data, isLoading);
    console.log("error", isError, error);
};

export default DashboardServer;

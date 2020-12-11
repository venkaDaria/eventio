import { StyleSheet } from "aphrodite";
import * as colors from "./colors";

const styles = StyleSheet.create({
    opacity50: {
        flexGrow: 0.5,
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        flexDirection: "column",
        minHeight: 200
    },
    boxShadow: {
        boxShadow: `0 0 80px 5px ${colors.dark_color}`
    }
});

export default styles;

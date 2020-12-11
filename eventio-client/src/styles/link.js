import { StyleSheet } from "aphrodite";
import * as colors from "./colors";

const styles = StyleSheet.create({
    default: {
        color: colors.text,
        ":active": {
            color: colors.text,
        },
        ":hover": {
            textDecoration: "underline"
        }
    }
});

export default styles;

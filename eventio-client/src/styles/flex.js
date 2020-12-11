import {StyleSheet} from "aphrodite";

import * as colors from "./colors";

const styles = StyleSheet.create({
    justFlex: {
        display: "flex"
    },
    container: {
        display: "flex",
        justifyContent: "space-around"
    },
    containerColumn: {
        display: "flex",
        "flex-direction": "column",
        justifyContent: "space-between"
    },
    bordered: {
        border: `solid 1px ${colors.text_color}`,
        "border-radius": 10,
        padding: 10
    },
    justify: {
        display: "flex",
        justifyContent: "space-around"
    },
    child: {
        minWidth: "40%"
    },
    backWhite: {
        backgroundColor: colors.white_opacity8
    },
    marginAuto: {
        margin: "auto"
    }
});

export default styles;

import { StyleSheet } from "aphrodite";

const styles = StyleSheet.create({
    scroll: {
        display: "flex",
        flexWrap: "nowrap",
        overflowX: "auto"
    },
    scrollBar: {
        overflow: "auto"
    },
    isPublic: {
        border: "4px double green"
    },
    isPrivate: {
        opacity: 0.9,
        border: "4px double red",
        paddingBottom: 0
    },
    card: {
        width: 420,
        height: "auto",
        flex: "0 0 auto",
        margin: 2,
        paddingBottom: 30
    },
    smallCard: {
        width: 300,
    },
    hidden: {
        display: "none"
    },
    grey: {
        color: "lightblue"
    }
});

export default styles;

import React from "react";
import * as colors from "../../styles/colors";
import {StyleSheet, css} from "aphrodite";

const styles = StyleSheet.create({
    eventBtn: {
        backgroundColor: colors.active_color,
        color: colors.text,
        maxHeight: 40,
        width: "100%",
        borderRadius: 0
    },

    hidden: {
        display: "none",
        ":focus": {
            outline: "none"
        }
    },

    shadow: {
        boxShadow: `0 6px 4px -4px ${colors.dark_color}`
    }
});

const style = (isNotAuth) => `${css(styles.eventBtn, styles.shadow)} btn
  ${isNotAuth ? css(styles.hidden) : ""}`;

const CreateEvent = ({translate, isAuth}) => <input className={ style(!isAuth) }
    type="button" value={ translate("create-event-upper") } onClick={ onClick }
/>;

const onClick = () => document.location.href = "/event";

export default CreateEvent;

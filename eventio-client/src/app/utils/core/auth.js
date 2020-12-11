export default class AuthUtils {
    constructor(active, isAuth){
        this.active = active;
        this.isAuth = isAuth;
    }

    isActive = (id) => this.active === id;

    isClickable = (onlyForAuth) => this.isAuth || !onlyForAuth;

    isNotClickable = (onlyForAuth) => !this.isClickable(onlyForAuth);

    elementMenu = (id, onlyForAuth = false) =>
        `item -link ${this.isActive(id) ? "-active" : ""}
      ${this.isNotClickable(onlyForAuth) ? "btn disabled": ""}`;

    spanElementMenu = (icon, onlyForAuth = false) =>
        `fa ${icon} ${(this.isClickable(onlyForAuth)) ? "text-info" : ""}`;
}

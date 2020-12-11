import React, {Fragment} from "react";
import {Redirect, Route} from "react-router";

import Menu from "../../components/menu/Menu";
import Profile from "../../components/auth/profile";
import ListElement from "../../components/utils/ListElement";
import PlaceList from "../../components/places/Places";
import PlaceDetails from "../../components/places/PlaceDetails";
import PlaceCard from "../../components/places/Card";
import EventCard from "../../components/events/Card";
import SaveEvent from "../../components/events/SaveEvent";
import CompanyCard from "../../components/companies/Card";
import ErrorPage from "../../components/ErrorPage";
import About from "../../components/About";
import Payment from "../../components/payment/Payment";
import History from "../../components/stats/History";
import AutoFixEvents from "../../components/events/AutoFixEvents";

import { go } from "../../reducers/page";
import { logoutUser } from "../../reducers/auth/actions";
import { getPathVariable } from "../core";
import { getData } from "../core/storage";
import { addEvent, updateEvent } from "../api/events";
import { translate, getCurrentLanguage } from "./locale";
import { onError } from "./alert";
import { getDateForApi } from "../core";

export const createRoutesMain = (store) => {
    const { auth, locale } = store.getState();
    const { isAuthenticated, user } = auth;

    const translateWithLocale = translate(locale);
    const currentLanguage = getCurrentLanguage(locale);
    const goDispatch = (page) => store.dispatch(go(page));

    const onErrorWithLogout = (xhr, alertMessage) => onError(xhr, () =>
        store.dispatch(logoutUser()), alertMessage);

    const AboutWrapper = () => <About currentLanguage={ currentLanguage } translate={ translateWithLocale }/>;

    const NotFoundWrapper = () => <ErrorPage text={ translateWithLocale("not-found") }
        errorCode={ 404 }
    />;

    const SecurityWrapper = () => <ErrorPage text={ translateWithLocale("security") }
        errorCode={ 401 }
    />;

    const ProfileWrapper = () => <Profile isCompany={ user.isCompany } translate={ translateWithLocale }
        currentLanguage={ currentLanguage } onError={ onErrorWithLogout } dispatch={ store.dispatch }
    />;

    const HistoryWrapper = () => <History
        translate={ translateWithLocale } onError={ onErrorWithLogout }
    />;

    const CreateEventWrapper = () => <SaveEvent onClick={ addEvent } isCompany={ user.isCompany }
        onError={ onErrorWithLogout } translate={ translateWithLocale } go={ goDispatch }
    />;

    const EditEventWrapper = () => <SaveEvent isEdit={ true } onClick={ updateEvent } id={ getPathVariable() }
        onError={ onErrorWithLogout } translate={ translateWithLocale } isCompany={ user.isCompany } go={ goDispatch }
    />;

    const PaymentWrapper = () => <Payment onError={ onErrorWithLogout }
        translate={ translateWithLocale }
    />;

    const PlacesWrapper = () => <PlaceList onError={ onErrorWithLogout }
        translate={ translateWithLocale }
    />;

    const PlaceDetailsWrapper = () => {
        return <PlaceDetails currentLanguage={ currentLanguage } id={ getPathVariable() }
            onError={ onErrorWithLogout } translate={ translateWithLocale }
        />;
    };

    const ListWrapper = (props) => <ListElement { ...props } translate={ translateWithLocale }
        isCompany={ user && user.isCompany } onError={ onErrorWithLogout } currentLanguage={ currentLanguage }
    />;

    const AutoFixEventsWrapper = () => <AutoFixEvents translate={ translateWithLocale }
        onError={ onErrorWithLogout } go={ goDispatch } />;

    const ListPlaceWrapper = () => {
        return <ListWrapper requestPath={ `/legal-person/${getPathVariable()}/places?` } card={ PlaceCard } />;
    };

    const ListPlaceSuggested = () => {
        const eventData = getData("eventData");

        if (!eventData) {
            return <NotFoundWrapper />;
        }

        const start = getDateForApi(eventData.start);
        const end = getDateForApi(eventData.end);

        const endPart = end? `end=${end}&` : "";

        return <ListWrapper card={ PlaceCard }
            requestPath={ `/event/places?start=${start}&onlyMine=${eventData.onlyMine}&${endPart}` }
        />;
    };

    const ListEventWrapper = () => <ListWrapper requestPath='/event?isPublic=true&'
        card={ EventCard } dateSearchBar={ true }
    />;

    const ListCompanyWrapper = () => <ListWrapper requestPath='/legal-person?' card={ CompanyCard } />;

    return <Fragment>
        <Redirect from="/" to="/en/"/>
        <Route path="/:locale(en|ru|ua)" component={ AboutWrapper }/>
        <Route path="/:locale(en|ru|ua)/events" component={ ListEventWrapper }/>
        <Route path="/:locale(en|ru|ua)/places" component={ isAuthenticated
            ? ListPlaceSuggested
            : SecurityWrapper }
        />
        <Route path="/:locale(en|ru|ua)/places/:id" component={ isAuthenticated
            ? PlaceDetailsWrapper
            : SecurityWrapper }
        />
        <Route path="/:locale(en|ru|ua)/companies" component={ isAuthenticated
            ? ListCompanyWrapper
            : SecurityWrapper }
        />
        <Route path="/:locale(en|ru|ua)/event" component={ isAuthenticated
            ? CreateEventWrapper
            : SecurityWrapper }
        />
        <Route path="/:locale(en|ru|ua)/event/:placeId" component={ isAuthenticated
            ? EditEventWrapper
            : SecurityWrapper }
        />
        <Route path="/:locale(en|ru|ua)/profile" component={ isAuthenticated
            ? ProfileWrapper
            : SecurityWrapper }
        />
        <Route path="/:locale(en|ru|ua)/profile/places" component={ isAuthenticated
            ? PlacesWrapper
            : SecurityWrapper }
        />
        <Route path="/:locale(en|ru|ua)/profile/places/:id" component={ isAuthenticated
            ? PlaceDetailsWrapper
            : SecurityWrapper }
        />
        <Route path="/:locale(en|ru|ua)/profile/remove-invalid-events" component={ isAuthenticated
            ? AutoFixEventsWrapper
            : SecurityWrapper }
        />
        <Route path="/:locale(en|ru|ua)/stats" component={ isAuthenticated
            ? HistoryWrapper
            : SecurityWrapper }
        />
        <Route path="/:locale(en|ru|ua)/payment" component={ isAuthenticated
            ? PaymentWrapper
            : SecurityWrapper }
        />
        <Route path="/:locale(en|ru|ua)/:companyUrl" component={ isAuthenticated
            ? ListPlaceWrapper
            : SecurityWrapper }
        />
        <Route path="/:locale(en|ru|ua)/:companyUrl/:placeId" component={ isAuthenticated
            ? PlaceDetailsWrapper
            : SecurityWrapper }
        />
        <Route path="/**" component={ NotFoundWrapper }/>
    </Fragment>;
};

export const createRoutesHeader = (store) => {
    const { locale, auth, page } = store.getState();
    const { isAuthenticated } = auth;

    const translateWithLocale = translate(locale);
    const currentLanguage = getCurrentLanguage(locale);

    const goDispatch = (page) => store.dispatch(go(page));

    const MenuWrapper = () => <Menu translate={ translateWithLocale }
        isAuth={ isAuthenticated } currentLanguage={ currentLanguage }
        activePage={ page.page } go={ goDispatch }
    />;

    return <Route path="/**" component={ MenuWrapper }/>;
};

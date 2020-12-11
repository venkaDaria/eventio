import _ from "lodash";
import moment from "moment";

const get = (el) => el && el.toString().toLowerCase();

const contains = (item, searchTerm) => _.includes(get(item), get(searchTerm));

const filter = (searchTerm) => (item) => _.some(item,
    el => contains(el, searchTerm) ||
  (typeof el === "object" && filter(searchTerm)(el))
);

export const filterBy = (entities, searchTerm) => _.filter(entities, filter(searchTerm));

const formatDate = "MM/DD/YYYY";

const filterDate = (searchTerm) => (item) => {
    const start = moment(item.start, formatDate).format(formatDate);
    const end = moment(item.end, formatDate).format(formatDate);
    return start === searchTerm || end === searchTerm;
};

export const filterByDate = (entities, searchDate) => {
    if (!searchDate) return entities;

    const searchTerm = searchDate.format(formatDate);
    return _.filter(entities, filterDate(searchTerm));
};

export const complexFilterBy = (entities, searchPairs) => _.filter(entities,
    (item) => _.some(
        searchPairs,
        (searchTerm, key) => contains(item[key], searchTerm) ||
        (typeof item[key] === "object" && filter(searchTerm)(item[key]))
    )
);

export const getPairs = (searchTerm, ...seps) => _.fromPairs(
    searchTerm.replace(seps[0], "").split(seps[1]).map(s => s.split(seps[2]))
);

export const filterByMap = (entities, searchTerm) => _.zipObject(
    _.map(filterByKeys(entities, searchTerm), el => JSON.stringify(el)),
    _.values(entities)
);

export const filterByKeys = (entities, searchTerm) => filterBy(
    _.map(_.keys(entities), el => JSON.parse(el)),
    searchTerm
);

export const sortBy = (entities, sortTerm) => _.orderBy(entities, [sortTerm], ["asc"]);

export const sortByKeys = (entities) => _.fromPairs(
    _.orderBy(_.toPairs(entities), (item) => JSON.parse(item[0]).title, ["asc"])
);

export const sortByValues = (entities) => _.fromPairs(
    _.orderBy(_.toPairs(entities), 1, ["asc"])
);

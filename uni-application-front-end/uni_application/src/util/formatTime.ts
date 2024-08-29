import moment from "moment-timezone";

export const formatTime = (time: any) => {
    const parsedDateUTC = moment.utc(time);
    return parsedDateUTC.format('MMMM Do YYYY, HH:mm');
}
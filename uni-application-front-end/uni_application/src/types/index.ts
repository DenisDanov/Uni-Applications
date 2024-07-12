import React from "react";

export type Empty = null | undefined;
export type StringOrEmpty = string | Empty;
export type WithChildren<T = {}> = T & { children?: React.ReactNode };

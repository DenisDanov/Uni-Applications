export type ReactHookFormAsyncData = {
  methods: any;
};

export type AsyncCallArgs = {
  promise: () => Promise<any>;
  onSuccess?: Function;
  onError?: Function;
  onValidationError?: Function;
  onSuccessArgs?: any[];
  onErrorArgs?: any[];
  withGlobalBackdrop?: boolean;
  reactHooksForm?: ReactHookFormAsyncData;
  processResponseErrors?: boolean;
  successMessage?: {
    show?: boolean;
    message?: string;
  };
  commonErrorMessage?: string;
};

import React, { useEffect } from 'react';
import LoadingPage from './LoadingPage';

const RedirectToUrl: React.FC<{ url: string }> = ({ url }) => {
    useEffect(() => {
        window.location.href = url;
    }, [url]);

    return <LoadingPage/>;
};

export default RedirectToUrl;

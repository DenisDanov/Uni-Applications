import React, {useState, useEffect} from 'react';
import {Box} from '@mui/material';
import {styled} from '@mui/system';
import {useLocation} from 'react-router-dom';
import LoadingPage from './LoadingPage';

// @ts-ignore
const BackgroundContainer = styled(Box)(({backgroundImage}) => ({
    minHeight: '100vh',
    display: 'flex',
    flexDirection: 'column',
    position: 'relative',
    color: '#fff',
    backgroundImage: backgroundImage ? `url(${backgroundImage})` : 'none',
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    backgroundRepeat: 'no-repeat',
}));

const Overlay = styled(Box)({
    position: 'absolute',
    top: 0,
    left: 0,
    width: '100%',
    height: '100%',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    zIndex: 1,
});

const MainContent = styled(Box)({
    zIndex: 2,
    width: '100%',
    flexGrow: 1,
    display: 'flex',
    flexDirection: 'column',
});

const BackgroundWrapper: React.FC<{ children: React.ReactNode }> = ({children}) => {
    const location = useLocation();
    const [imageLoaded, setImageLoaded] = useState(false);
    const [backgroundImage, setBackgroundImage] = useState<string | null>(null);

    const isHomeRoute = location.pathname === '/';

    useEffect(() => {
        const img = new Image();
        img.src = '/20944371.avifs';

        img.onload = () => {
            const canvas = document.createElement('canvas');
            const ctx = canvas.getContext('2d');
            if (ctx) {
                canvas.width = img.width;
                canvas.height = img.height;
                ctx.drawImage(img, 0, 0);
                const dataURL = canvas.toDataURL('image/avif');
                setBackgroundImage(dataURL);
                setImageLoaded(true);
            }
        };

        img.onerror = () => setImageLoaded(false);
    }, []);

    if (isHomeRoute && !imageLoaded) {
        return <LoadingPage/>;
    }

    return isHomeRoute ? (
        <BackgroundContainer style={{
            backgroundImage: `url(${backgroundImage})`,
        }}>
            <Overlay/>
            <MainContent>
                {children}
            </MainContent>
        </BackgroundContainer>
    ) : (
        <MainContent>{children}</MainContent>
    );
};

export default BackgroundWrapper;

import styled from "styled-components";

const OuterDiv = styled.div`
    display: inline-flex;
    position: fixed;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    z-index: 10001;
    visibility: visible;
    top: 0;
    margin-top: -150px;
`;

const InnerDiv = styled.div`
    display: inline-block;
    width: 80px;
    height: 80px;
`;
// const RelativeDiv = styled.div`
//   position: relative;
// `;
//
// const LogoImg = styled.img`
//   width: 50px;
//   margin-left: 20px;
//   position: absolute;
//   margin-top: 15px;
// `;

const LoadingDiv = styled.div`
    position: absolute;
    margin-top: 100px;
    color: #059;
    font-size: 1rem;
    margin-left: -5px;
    font-family: verdana;
`;

const LoadingPage = () => {
    return (
        <OuterDiv>
            <InnerDiv>
                {/*<RelativeDiv>*/}
                {/*  <LogoImg src={Logo} />*/}
                {/*</RelativeDiv>*/}
                <LoadingDiv>Loading...</LoadingDiv>
            </InnerDiv>
        </OuterDiv>
    );
};
export default LoadingPage;

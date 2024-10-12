import React from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import SecurityGuard from '../security/SecurityGuard';
import Specialties from "./Specialties";
import Faculties from "./Faculties";
import Application from "./Application";
import Home from "./Home";
import Profile from "./Profile";
import ManageStudentApplications from "./ManageStudentApplications";
import ManageUsers from "./ManageUsers";
import EvaluateApplication from "./EvaluateApplication";
import RequirementsTests from "./RequirementsTests";
import AdminDashboard from "./AdminDashboard";
import News from "./News";

const HomeRoute: React.FC = () => <Home />;
const ProfileRoute: React.FC = () => <Profile />;
const FacultiesRoute: React.FC = () => <Faculties />;
const SpecialtiesRoute: React.FC = () => <Specialties />;
const Apply: React.FC = () => <Application />
const ManageStudentApplicationsRoute: React.FC = () => <ManageStudentApplications />;
const ManageUsersRoute: React.FC = () => <ManageUsers />;
const EvaluateApplicationRoute: React.FC = () => <EvaluateApplication />
const RequirementsTestsRoute: React.FC = () => <RequirementsTests />
const AdminDashboardRoute: React.FC = () => <AdminDashboard />
const NewsRoute: React.FC = () => <News />
const NotFoundRoute: React.FC = () => <div>404 - Page Not Found</div>;

const routes = [
    {
        path: "/",
        element: <HomeRoute />,
    },
    {
        path: "/faculties",
        element: <FacultiesRoute />,
    },
    {
        path: "/specialties",
        element: <SpecialtiesRoute />,
    },
    {
        path: "/profile",
        element: (
            <SecurityGuard
                doCheck={true}
                checkForRoles={["admin", "student"]}
                roleOperator="or"
                loginOnUnauthorized={false}
                displayOnUnauthorized={<Navigate to={"/"} />}
            >
                <ProfileRoute />
            </SecurityGuard>
        ),
    },
    {
        path: "/apply",
        element: (
            <SecurityGuard
                doCheck={true}
                checkForRoles={['student']}
                loginOnUnauthorized={true}
                displayOnUnauthorized={<Navigate to={"/"} />}
            >
                <Apply />
            </SecurityGuard>
        ),
    },
    {
        path: "/manage-applications",
        element: (
            <SecurityGuard
                doCheck={true}
                checkForRoles={['admin']}
                loginOnUnauthorized={false}
                displayOnUnauthorized={<Navigate to="/" />}
            >
                <ManageStudentApplicationsRoute />
            </SecurityGuard>
        )
    },
    {
        path: "/manage-users",
        element: (
            <SecurityGuard
                doCheck={true}
                checkForRoles={['admin']}
                loginOnUnauthorized={false}
                displayOnUnauthorized={<Navigate to="/" />}
            >
                <ManageUsersRoute />
            </SecurityGuard>
        )
    },
    {
        path: "/evaluate-application",
        element: (
            <SecurityGuard
                doCheck={true}
                checkForRoles={['admin']}
                loginOnUnauthorized={false}
                displayOnUnauthorized={<Navigate to="/" />}
            >
                <EvaluateApplicationRoute />
            </SecurityGuard>
        )
    },
    {
        path: "/admin-dashboard",
        element: (
            <SecurityGuard
                doCheck={true}
                checkForRoles={['admin']}
                loginOnUnauthorized={false}
                displayOnUnauthorized={<Navigate to="/" />}
            >
                <AdminDashboardRoute />
            </SecurityGuard>
        )
    },
    {
        path: "/requirements-tests",
        element: (
            <SecurityGuard
                doCheck={true}
                checkForRoles={['student']}
                loginOnUnauthorized={false}
                displayOnUnauthorized={<Navigate to="/" />}
            >
                <RequirementsTestsRoute />
            </SecurityGuard>
        )
    },
    {
        path: "/news",
        element: <NewsRoute />
    },
    {
        path: "*",
        element: <NotFoundRoute />,
    },
];

export default routes;
